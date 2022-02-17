package finki.ukim.kgt.kgtfeedback.models

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Feedback(
    @Column(name = "id", length = 32)
    @GeneratedValue(generator = "strategy-uuid")
    @GenericGenerator(name = "strategy-uuid", strategy = "uuid")
    @Id
    var id: String? = null,

    @Column(name = "generated_text", columnDefinition = "text")
    var generatedText: String? = null,

    @Column(name = "corrected_text", columnDefinition = "text")
    var correctedText: String? = null,

    @Column(name = "corrected_by_user", length = 32)
    var correctedByUserId: String? = null,

    @Column(name = "related_node", length = 32)
    var relatedNodeId: String? = null
)
