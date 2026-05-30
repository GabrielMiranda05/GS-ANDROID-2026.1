package br.com.windfyr

import android.app.Application
import br.com.windfyr.data.remote.MockDataStore

class WindfyrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MockDataStore.init(this)
    }
}
