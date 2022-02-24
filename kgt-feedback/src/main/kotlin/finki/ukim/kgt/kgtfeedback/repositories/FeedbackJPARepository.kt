package finki.ukim.kgt.kgtfeedback.repositories

import finki.ukim.kgt.kgtfeedback.models.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface FeedbackJPARepository: JpaRepository<Feedback, String>, QuerydslPredicateExecutor<Feedback>