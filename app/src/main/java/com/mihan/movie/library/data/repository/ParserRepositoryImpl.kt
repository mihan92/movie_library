package com.mihan.movie.library.data.repository

import android.util.ArrayMap
import android.util.Base64
import com.mihan.movie.library.common.Constants.BASE_URL
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.data.models.StreamDto
import com.mihan.movie.library.data.models.VideoDetailDto
import com.mihan.movie.library.data.models.VideoDto
import com.mihan.movie.library.data.models.VideoItemDto
import com.mihan.movie.library.domain.ParserRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

@ActivityRetainedScoped
class ParserRepositoryImpl @Inject constructor() : ParserRepository {

    /**
     * Кэшируем список фильмов после первой загрузки, чтобы при каждом обращении не качать его снова.
     * Если текущие данные совпадают, то отдаем лист с кэшированными данными, если нет, качаем порцию фильмов снова.
     */
    private val cashedListVideo = mutableListOf<VideoItemDto>()
    private var currentPage = -1
    private var currentFilter = Filter.Watching
    private var currentVideoCategory = VideoCategory.All

    private fun getConnection(filmUrl: String): Connection = Jsoup
        .connect(filmUrl)
        .ignoreContentType(true)
        .header(APP_HEADER, REQUEST_HEADER_ENABLE_METADATA_VALUE)
        .timeout(CONNECTION_TIMEOUT)

    private fun getUrl(page: Int, section: String, genre: String): String {
        return if (page > 1)
            "$BASE_URL/page/$page/?filter=$section$genre"
        else
            "$BASE_URL/?filter=$section$genre"
    }

    /**
     * Из главной страницы получаем список видосов
     */
    override suspend fun getListVideo(filter: Filter, videoCategory: VideoCategory, page: Int): List<VideoItemDto> =
        withContext(Dispatchers.IO) {
            if(currentFilter == filter && currentVideoCategory == videoCategory && currentPage == page) {
                cashedListVideo.toList()
            } else {
                cashedListVideo.clear()
                val document = getConnection(getUrl(page, filter.section, videoCategory.genre)).get()
                val element = document.select("div.b-content__inline_item")
                for (i in 0 until element.size) {
                    val title = element.select("div.b-content__inline_item-link")
                        .select("a")
                        .eq(i)
                        .text()
                    val imageUrl = element.select("img")
                        .eq(i)
                        .attr("src")
                    val movieUrl = element.select("div.b-content__inline_item-cover")
                        .select("a")
                        .eq(i)
                        .attr("href")
                    val category = element.select("i.entity")
                        .eq(i)
                        .text()
                    val movie = VideoItemDto(
                        title = title,
                        category = category,
                        imageUrl = imageUrl,
                        videoUrl = movieUrl
                    )
                    cashedListVideo.add(movie)
                }
                currentFilter = filter
                currentVideoCategory = videoCategory
                currentPage = page
                cashedListVideo.toList()
            }
        }

    /**
     * Получаем всю детальную инфу о видосе и выводим на экране детальной информации
     */
    override suspend fun getDetailVideoByUrl(url: String): VideoDetailDto = withContext(Dispatchers.IO) {
        val document = getConnection(url).get()
        val element = document.select("div.b-post")
        val title = element.select("div.b-post__title").text()
        val desc = element.select("div.b-post__description_text").text()
        val releaseDate = element.select("tr").eq(3).text()
        val country = element.select("tr").eq(4).text()
        val genre = element.select("tr").eq(6).text()
        val ratingIMdb = element.select("tr").eq(0).text()
        val ratingHdRezka = element.select("span.num").text() + element.select("span.votes").text()
        val imageUrl = element.select("img").attr("src")
        VideoDetailDto(
            title = title,
            description = desc,
            releaseDate = releaseDate,
            country = country,
            ratingIMdb = ratingIMdb,
            ratingHdrezka = ratingHdRezka,
            genre = genre,
            imageUrl = imageUrl
        )
    }

    /**
     * Проходим по странице и ищем переводы, если переводы не найдены, значит это оригинальная озвучка без переводов.
     * Добавляем ее вручную
     */
    private fun getTranslations(document: Document): Map<String, String> {
        val mapOfTraslations = mutableMapOf<String, String>()
        val translators = document.select(".b-translator__item")
        for (element in translators) {
            val country = element.select("img").attr("alt")
            var name = element.attr("title")
            val translatorId = element.attr("data-translator_id")
            if (country.isNotEmpty()) {
                name += "($country)"
            }
            mapOfTraslations[name] = translatorId
        }
        if (mapOfTraslations.isEmpty())
            mapOfTraslations[RUSSIAN_TRANSLATOR_NAME] = RUSSIAN_TRANSLATOR_ID
        return mapOfTraslations.toMap()
    }

    override suspend fun getTranslationsByUrl(url: String): VideoDto = withContext(Dispatchers.IO) {
        val document = getConnection(url).get()
        val filmId = document.select("div.b-userset__fav_holder").attr("data-post_id")
        val isVideoHasTranslation = document.select("div.b-translators__block").hasText()
        val translations = getTranslations(document)
        val isVideoHasSeries = document.select("ul.b-simple_seasons__list").hasText()
        VideoDto(
            videoId = filmId,
            isVideoHasTranslations = isVideoHasTranslation,
            isVideoHasSeries = isVideoHasSeries,
            translations = translations,
            videoStreamsWithTranslatorName = getStreamsByUrl(
                document,
                isVideoHasTranslation,
                translations,
                isVideoHasSeries
            ),
            seasonList = getSeasons(filmId, translations)
        )
    }

    override suspend fun getStreamsBySeasonId(
        translationId: String,
        videoId: String,
        season: String,
        episode: String
    ): List<StreamDto> = withContext(Dispatchers.IO) {
        val list = mutableListOf<StreamDto>()
        val data: ArrayMap<String, String> = ArrayMap()
        data["id"] = videoId
        data["translator_id"] = translationId
        data["season"] = season
        data["episode"] = episode
        data["action"] = "get_stream"
        val unixTime = System.currentTimeMillis()
        val result: Document? = getConnection("$BASE_URL$GET_STREAM_POST/?t=$unixTime").data(data).post()
        if (result != null) {
            val bodyString: String = result.select("body").text()
            val jsonObject = JSONObject(bodyString)
            val streams = parseSteams(jsonObject.getString("url"))
            list.addAll(streams)
        }
        list.toList()
    }

    override suspend fun getVideosByTitle(videoTitle: String): List<VideoItemDto> = withContext(Dispatchers.IO) {
        val list = mutableListOf<VideoItemDto>()
        val document = getConnection(BASE_URL + SEARCH_URL).data("q", videoTitle).post()
        val element = document.select("div.b-content__inline_item")
        for (i in 0 until element.size) {
            val title = element.select("div.b-content__inline_item-link")
                .select("a")
                .eq(i)
                .text()
            val imageUrl = element.select("img")
                .eq(i)
                .attr("src")
            val movieUrl = element.select("div.b-content__inline_item-cover")
                .select("a")
                .eq(i)
                .attr("href")
            val category = element.select("i.entity")
                .eq(i)
                .text()
            val movie = VideoItemDto(
                title = title,
                category = category,
                imageUrl = imageUrl,
                videoUrl = movieUrl
            )
            list.add(movie)
        }
        list.toList()
    }

    private suspend fun getStreamsByUrl(
        document: Document,
        isVideoHasTranslation: Boolean,
        translators: Map<String, String>,
        isVideoHasSeries: Boolean
    ): Map<String, List<StreamDto>> = withContext(Dispatchers.IO) {
        val mapOfVideos = mutableMapOf<String, List<StreamDto>>()
        val listOfStreams = mutableListOf<StreamDto>()
        val filmId = document.select("div.b-userset__fav_holder").attr("data-post_id")
        if (isVideoHasTranslation) {
            translators.entries.forEach { entry ->
                val translations = getStreamsByTranslationId(filmId, entry.value)
                if (translations.isNotEmpty())
                    mapOfVideos[entry.key] = translations
            }
        } else {
            if (isVideoHasSeries) {
                emptyMap<String, List<StreamDto>>()
            } else {
                val stringedDoc = document.toString()
                val index = stringedDoc.indexOf("initCDNMoviesEvents")
                if (index != -1) {
                    val subString = stringedDoc.substring(
                        stringedDoc.indexOf("{\"id\"", index),
                        stringedDoc.indexOf("});", index) + 1
                    )
                    val jsonObject = JSONObject(subString)
                    val parsed = parseSteams(jsonObject.getString("streams"))
                    listOfStreams.addAll(parsed)
                    mapOfVideos["default"] = listOfStreams.toList()
                } else {
                    error("Видео ещё не добавлено. Ожидайте...")
                }
            }
        }
        mapOfVideos.toMap()
    }

    private suspend fun getStreamsByTranslationId(filmId: String, translatorId: String): List<StreamDto> =
        withContext(Dispatchers.IO) {
            val listOfStreams = mutableListOf<StreamDto>()
            val data: ArrayMap<String, String> = ArrayMap()
            data["id"] = filmId
            data["translator_id"] = translatorId
            data["action"] = "get_movie"
            val unixTime = System.currentTimeMillis()
            val result: Document? = getConnection("$BASE_URL$GET_STREAM_POST/?t=$unixTime").data(data).post()
            if (result != null) {
                val bodyString: String = result.select("body").text()
                val jsonObject = JSONObject(bodyString)
                if (jsonObject.getBoolean("success")) {
                    listOfStreams.addAll(parseSteams(jsonObject.getString("url")))
                }
            } else {
                error("Видео недоступно")
            }
            listOfStreams
        }

    private fun parseSteams(streams: String?): List<StreamDto> {
        val parsedStreams = mutableListOf<StreamDto>()
        if (!streams.isNullOrEmpty()) {
            val decodedStreams = decodeUrl(streams)
            val split: Array<String> = decodedStreams.split(",").toTypedArray()
            for (str in split) {
                try {
                    if (str.contains(" or ")) {
                        parsedStreams.add(
                            StreamDto(
                                str.split(" or ").toTypedArray()[1],
                                str.substring(1, str.indexOf("]"))
                            )
                        )
                    } else {
                        parsedStreams.add(
                            StreamDto(
                                str.substring(str.indexOf("]") + 1),
                                str.substring(1, str.indexOf("]"))
                            )
                        )
                    }
                } catch (e: Exception) {
                    error("${e.message}")
                }
            }
        }
        return parsedStreams
    }

    private fun getSeasons(filmId: String, translators: Map<String, String>): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        translators.values.forEach { translatorId ->
            val data: ArrayMap<String, String> = ArrayMap()
            data["id"] = filmId
            data["translator_id"] = translatorId
            data["action"] = "get_episodes"
            val unixTime = System.currentTimeMillis()
            val result: Document? = getConnection("$BASE_URL$GET_STREAM_POST/?t=$unixTime").data(data).post()
            if (result != null) {
                val bodyString: String = result.select("body").text()
                val jsonObject = JSONObject(bodyString)
                if (jsonObject.getBoolean("success")) {
                    map.putAll(parseSeasons(Jsoup.parse(jsonObject.getString("episodes"))))
                }
            } else {
                error("result is null")
            }
        }
        return map.toMap()
    }

    private fun parseSeasons(document: Document): Map<String, List<String>> {
        val seasonList = mutableMapOf<String, List<String>>()
        val seasons = document.select("ul.b-simple_episodes__list")
        for (season in seasons) {
            val n = season.attr("id").replace("simple-episodes-list-", "")
            val episodesList: ArrayList<String> = ArrayList()
            val episodes = season.select("li.b-simple_episode__item")
            for (episode in episodes) {
                episodesList.add(episode.attr("data-episode_id"))
            }
            seasonList[n] = episodesList
        }
        return seasonList.toMap()
    }

    private fun decodeUrl(str: String): String {
        return try {
            if (!str.startsWith("#h")) {
                return str
            }
            var replace = str.replace("#h", "")
            var i = 0
            while (i < 20 && replace.contains("//_//")) {
                val indexOf = replace.indexOf("//_//")
                if (indexOf > -1) {
                    replace = replace.replace(replace.substring(indexOf, indexOf + 21), "")
                }
                i++
            }
            String(Base64.decode(replace, 0))
        } catch (e: Exception) {
            e.printStackTrace()
            str
        }
    }

    companion object {
        private const val CONNECTION_TIMEOUT = 15_000
        private const val REQUEST_HEADER_ENABLE_METADATA_VALUE = "1"
        private const val APP_HEADER = "X-App-Hdrezka-App"
        private const val GET_STREAM_POST = "/ajax/get_cdn_series"
        private const val RUSSIAN_TRANSLATOR_NAME = "Оригинальный"
        private const val RUSSIAN_TRANSLATOR_ID = "110"
        private const val SEARCH_URL = "/search/?do=search&subaction=search"
    }
}