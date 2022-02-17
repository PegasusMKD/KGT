package finki.ukim.kgt.kgtfeedback.dtos

data class FeedbackDto(
    var id: String?,
    var generatedText: String?,
    var correctedText: String?,
    var correctedByUserId: String?,
    var relatedNodeId: String?,
    var queryText: String?
)
