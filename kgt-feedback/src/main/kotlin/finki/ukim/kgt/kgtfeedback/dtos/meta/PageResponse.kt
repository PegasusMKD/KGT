package finki.ukim.kgt.kgtfeedback.dtos.meta

class PageResponse<T>(private val totalPages: Int, private val totalElements: Int, private val content: List<T>?) {
    constructor(totalPages: Int, totalElements: Long, content: List<T>?) : this(
        totalPages,
        totalElements.toInt(),
        content
    )
}