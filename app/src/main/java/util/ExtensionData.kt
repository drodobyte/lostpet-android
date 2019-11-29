package util

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import java.util.*

fun Triple<Int, Int, Int>.asDate(): Date = Calendar.getInstance().apply {
    set(Calendar.YEAR, first)
    set(Calendar.MONTH, second)
    set(Calendar.DAY_OF_MONTH, third)
}.time

fun Date.asCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}

fun <T> Observable<T>.onIO(): Observable<T> = subscribeOn(io()).observeOn(mainThread())
fun <T> Maybe<T>.onIO() = subscribeOn(io()).observeOn(mainThread())
fun <T> Single<T>.onIO() = subscribeOn(io()).observeOn(mainThread())
