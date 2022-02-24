package finki.ukim.kgt.kgtfeedback.controllers

import finki.ukim.kgt.kgtfeedback.dtos.FeedbackDto
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageRequestByExample
import finki.ukim.kgt.kgtfeedback.dtos.meta.PageResponse
import finki.ukim.kgt.kgtfeedback.services.FeedbackService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RestController
class FeedbackController(private val feedbackService: FeedbackService) {

    private val logger: Logger = LoggerFactory.getLogger(FeedbackController::class.java)

    @PostMapping("/", produces = [APPLICATION_JSON_VALUE], consumes = [APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody dto: FeedbackDto?,
        @RequestHeader(name = "Roles", required = true) roles: List<String>,
        @RequestHeader(name = "User-ID", required = true) userID: String
    ): ResponseEntity<FeedbackDto> {
        // TODO: Implement check as interceptor with custom annotations
        if (!roles.contains("MODERATOR")) {
            logger.debug("Someone tried to create feedback without a proper role.")
            return ResponseEntity.badRequest().build()
        }
        logger.debug("Created Feedback: {}", dto)
        if (dto?.id != null)
            return ResponseEntity.badRequest()
                .header("Error", "Cannot create feedback with an existing ID")
                .body(null)

        dto?.correctedByUserId = userID
        val result = feedbackService.save(dto)
        return result?.let { ResponseEntity.ok(it) } ?: ResponseEntity.badRequest().build()
    }

    @PutMapping("/", produces = [APPLICATION_JSON_VALUE], consumes = [APPLICATION_JSON_VALUE])
    fun update(
        @RequestBody dto: FeedbackDto?,
        @RequestHeader(name = "Roles", required = true) roles: List<String>,
        @RequestHeader(name = "User-ID", required = true) userID: String
    ): ResponseEntity<FeedbackDto> {
        // TODO: Implement check as interceptor with custom annotations
        if (!roles.contains("MODERATOR")) {
            logger.debug("Someone tried to update feedback without a proper role.")
            return ResponseEntity.badRequest().build()
        }
        logger.debug("Updated Feedback: {}", dto)
        if (dto?.id == null)
            return create(dto, roles, userID)

        val result = feedbackService.save(dto)
        return result?.let { ResponseEntity.ok(it) } ?: ResponseEntity.badRequest().build()
    }

    @GetMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    fun findById(@PathVariable id: String): ResponseEntity<FeedbackDto?> {
        logger.debug("Searching for feedback with ID: {}", id)
        val result = feedbackService.findOne(id)
        return if (result != null) ResponseEntity.ok(result) else ResponseEntity.notFound().build()
    }

    @PostMapping("/page", produces = [APPLICATION_JSON_VALUE])
    fun findAll(@RequestBody prbe: PageRequestByExample<FeedbackDto?>): ResponseEntity<PageResponse<FeedbackDto?>> {
        logger.debug("Searching feedback by example: {}", prbe.example)
        return ResponseEntity.ok(feedbackService.findAll(prbe))
    }

    @DeleteMapping("/{id}", produces = [APPLICATION_JSON_VALUE])
    fun delete(
        @PathVariable id: String,
        @RequestHeader(name = "Roles", required = true) roles: List<String>
    ): ResponseEntity<Void> {
        // TODO: Implement check as interceptor with custom annotations
        if (!roles.contains("MODERATOR")) {
            logger.debug("Someone tried to delete feedback without a proper role.")
            return ResponseEntity.badRequest().build()
        }
        logger.debug("Trying to delete feedback with ID: {}", id)
        return try {
            feedbackService.deleteById(id)
            ResponseEntity.ok().build()
        } catch (_: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}