import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.SECONDS

internal class RetrofitCoroutineHangTests {

  private val client: OkHttpClient = OkHttpClient
    .Builder()
    .apply {
      networkInterceptors()
        .add(Interceptor { _ -> throw IllegalStateException("💩🛏") })
    }
    .build()

  private val retrofit: Retrofit = Retrofit
    .Builder()
    .client(client)
    .baseUrl("https://httpbin.org/")
    .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
    .build()

  @Test
  fun `exception in network interceptor should not hang coroutine`() {
    val service = retrofit
      .create(CoroutineEnabledRetrofitService::class.java)

    val latch = CountDownLatch(1)

    GlobalScope.launch {
      try {
        service.json()
      } finally {
        latch.countDown()
      }
    }

    assertTrue(latch.await(2, SECONDS)) { "Coroutine completed" }
  }
}

interface CoroutineEnabledRetrofitService {
  @GET("/json")
  suspend fun json(): Map<String, Any?>
}
