package util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AppPresenter {

    private val disposables = CompositeDisposable()

    fun Disposable.add() {
        disposables.add(this)
    }

    fun dispose() {
        disposables.clear()
    }
}