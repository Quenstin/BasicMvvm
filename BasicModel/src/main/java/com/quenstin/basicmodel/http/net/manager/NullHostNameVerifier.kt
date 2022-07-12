package com.hdyj.basicmodel.http.net.manager

import android.annotation.SuppressLint
import android.util.Log
import com.hdyj.basicmodel.utils.hdyjLogi
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


/**
 * Created by DanYue on 2022/3/8 15:08.
 */
class NullHostNameVerifier : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession?): Boolean {
        hdyjLogi("RestUtilImpl", "Approving certificate for $hostname")
        return true
    }

    companion object {
        fun getSocketFactory(): SSLSocketFactory {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("TrustAllX509TrustManager")
            val trustAllCerts =
                arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOfNulls(0)
                    }

                    override fun checkClientTrusted(
                        certs: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        certs: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }
                })
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            return sc.socketFactory
        }
    }
}

