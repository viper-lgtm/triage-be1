package cz.martinvedra.triagebe1.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "exchange_rate_snapshot")
data class ExchangeRateSnapshot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "feed_id", nullable = false)
    val feedId: String,

    @Column(name = "payload", columnDefinition = "text", nullable = false)
    val payload: String,

    @Column(name = "payload_hash", nullable = false)
    val payloadHash: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)