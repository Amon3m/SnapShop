
import com.itigradteamsix.snapshop.settings.currency.CurrencyApiService
import javax.inject.Inject

class ApiDataSource(private val apiService: CurrencyApiService) {

    suspend fun getConvertedRate(access_key: String, from: String, to: String, amount: Double) =
        apiService.convertCurrency(access_key, from, to, amount)

}