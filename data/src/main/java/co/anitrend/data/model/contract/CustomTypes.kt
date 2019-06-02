package co.anitrend.data.model.contract

/**
 * 8 digit long date integer (YYYYMMDD).
 * Unknown dates represented by 0.
 *
 * E.g. 2016: 20160000
 * May 1976: 19760500
 */
typealias FuzzyDateInt = String

/**
 * A query filter type for FuzzyDateInt,
 * instead of return YYYYMMDD any unset fields
 * are replaced by %
 *
 * E.g 2019: 2019%
 * May 2011: 201105%
 */
typealias FuzzyDateLike = String