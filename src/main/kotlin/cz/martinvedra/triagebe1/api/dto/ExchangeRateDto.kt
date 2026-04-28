package cz.martinvedra.triagebe1.api.dto

import java.math.BigDecimal
import java.time.LocalDateTime

@JvmRecord
data class ExchangeRateDto(
    val shortName: String?,
    val validFrom: LocalDateTime?,
    val name: String?,
    val country: String?,
    val move: BigDecimal?,
    val amount: BigDecimal?,
    val valBuy: BigDecimal?,
    val valSell: BigDecimal?,
    val valMid: BigDecimal?,
    val currBuy: BigDecimal?,
    val currSell: BigDecimal?,
    val currMid: BigDecimal?,
    val version: Int?,
    val cnbMid: BigDecimal?,
    val ecbMid: BigDecimal?
)
