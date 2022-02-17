package finki.ukim.kgt.kgtfeedback.repositories

import com.querydsl.core.types.Predicate
import finki.ukim.kgt.kgtfeedback.models.Feedback
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.NonNull

interface FeedbackJPARepository: JpaRepository<Feedback, String> {

    @NonNull
    fun findAll(@NonNull predicate: Predicate?, @NonNull pageable: Pageable?): Page<Feedback?>
}