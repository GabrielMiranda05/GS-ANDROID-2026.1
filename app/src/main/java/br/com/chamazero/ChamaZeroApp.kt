package br.com.chamazero

import android.app.Application
import br.com.chamazero.data.remote.MockDataStore

class ChamaZeroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MockDataStore.init(this)
    }
}
