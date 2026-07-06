package cz.martinvedra.triagebe1.repository

import cz.martinvedra.triagebe1.model.ExchangeRateSnapshot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRateSnapshotRepository : JpaRepository<ExchangeRateSnapshot, Long> {
    fun findTopByFeedIdOrderByCreatedAtDesc(feedId: String): ExchangeRateSnapshot?
}
