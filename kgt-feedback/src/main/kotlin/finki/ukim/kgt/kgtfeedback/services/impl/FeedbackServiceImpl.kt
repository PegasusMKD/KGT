package finki.ukim.kgt.kgtfeedback.services.impl

import com.querydsl.core.types.dsl.BooleanExpression
import finki.ukim.kgt.kgtfeedback.dtos.FeedbackDto
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageRequestByExample
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageResponse
import finki.ukim.kgt.kgtfeedback.mappers.FeedbackMapper
import finki.ukim.kgt.kgtfeedback.models.Feedback
import finki.ukim.kgt.kgtfeedback.models.QFeedback
import finki.ukim.kgt.kgtfeedback.repositories.FeedbackJPARepository
import finki.ukim.kgt.kgtfeedback.services.FeedbackService
import finki.ukim.kgt.kgtfeedback.services.util.OptionalBooleanBuilder
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import java.util.stream.Collectors
import org.springframework.stereotype.Service

@Primary
@Service
class FeedbackServiceImpl(
    private val feedbackJPARepository: FeedbackJPARepository,
    private val feedbackMapper: FeedbackMapper
) : FeedbackService {

    override fun findOne(id: String): FeedbackDto? {
        return feedbackJPARepository.findById(id).map(feedbackMapper::toDto)
            .orElseThrow { Exception("Feedback does not exist.") }
    }

    override fun save(dto: FeedbackDto?): FeedbackDto? {
        if (dto == null) {
            return null
        }
        val entity: Feedback =
            if (dto.id != null && dto.id!!.isNotEmpty()) feedbackJPARepository.findById(dto.id!!)
                .orElseThrow { Exception("No Feedback found for given id.") }
            else Feedback()


        feedbackMapper.updateEntity(dto, entity)

        return feedbackMapper.toDto(
            feedbackJPARepository.save(
                entity
            )
        )
    }


    override fun findAll(prbe: PageRequestByExample<FeedbackDto?>): PageResponse<FeedbackDto?> {
        val dto: FeedbackDto? = prbe.example
        val pageable = prbe.toPageable()
        val page = if (dto != null)
            feedbackJPARepository.findAll(makeFilter(dto), pageable)
        else Page.empty(pageable)

        val content = page.content.stream()
            .map { feedback -> feedbackMapper.toDto(feedback) }
            .collect(Collectors.toList())

        return PageResponse(page.totalPages, page.totalElements.toInt(), content)
    }

    override fun makeFilter(dto: FeedbackDto?): BooleanExpression {
        val qFeedback = QFeedback.feedback
        val opBuilder = OptionalBooleanBuilder.builder(qFeedback.isNotNull)
        if (dto == null)
            return opBuilder.build()

        return opBuilder
            .notEmptyAnd(qFeedback.id::eq, dto.id)
            .notEmptyAnd(qFeedback.relatedNodeId::eq, dto.relatedNodeId)
            .notEmptyAnd(qFeedback.correctedByUserId::eq, dto.correctedByUserId)

            .notEmptyAnd(qFeedback.correctedText::contains, dto.queryText)
            .notEmptyAnd(qFeedback.generatedText::contains, dto.queryText)

            .build()
    }

    override fun deleteById(id: String) {
        feedbackJPARepository.deleteById(id)
    }

}