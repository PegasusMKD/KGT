package finki.ukim.kgt.kgtfeedback.services

import com.querydsl.core.types.dsl.BooleanExpression
import finki.ukim.kgt.kgtfeedback.dtos.FeedbackDto
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageRequestByExample
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageResponse
import org.springframework.stereotype.Service

@Service
interface FeedbackService {
    fun findOne(id: String): FeedbackDto?
    fun save(dto: FeedbackDto?): FeedbackDto?
    fun deleteById(id: String)
    fun findAll(prbe: PageRequestByExample<FeedbackDto?>): PageResponse<FeedbackDto?>
    fun makeFilter(dto: FeedbackDto?): BooleanExpression?
}
