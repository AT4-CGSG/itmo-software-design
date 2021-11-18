package servlet

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import ru.atsutsiev.sd.refactoring.database.models.Products
import ru.atsutsiev.sd.refactoring.database.models.custom_fields.ProductDataRecord
import ru.atsutsiev.sd.refactoring.servlet.AddProductServlet
import ru.atsutsiev.sd.refactoring.servlet.GetProductsServlet
import ru.atsutsiev.sd.refactoring.servlet.ProductsServlet
import ru.atsutsiev.sd.refactoring.servlet.QueryServlet
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.SQLException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author atsutsuiev
 */
class ProductServletsTest {
    @Mock
    private val productsModel: Products? = null

    private var addServlet: ProductsServlet? = null
    private var getServlet: ProductsServlet? = null
    private var queryServlet: ProductsServlet? = null

    @Mock
    private val servletRequest: HttpServletRequest? = null

    @Mock
    private val servletResponse: HttpServletResponse? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addServlet = AddProductServlet(productsModel!!)
        getServlet = GetProductsServlet(productsModel)
        queryServlet = QueryServlet(productsModel)
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testAdd() {
        val product = ProductDataRecord("lozhka", 1)
        `when`(servletRequest!!.getParameter("name")).thenReturn(product.name)
        `when`(servletRequest.getParameter("price")).thenReturn("${product.price}")

        runMockedServlet(addServlet!!, "OK")
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testGetAll() {
        val products = listOf(ProductDataRecord("vilka", 2), ProductDataRecord("lozhka", 1))
        `when`(productsModel!!.all()).thenReturn(products)

        runMockedServlet(
            getServlet!!,
            "<html><body>"+
                    "<br>${products[0].name}\t${products[0].price}</br>" +
                    "<br>${products[1].name}\t${products[1].price}</br>" +
                    "</body></html>"
        )
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testMax() {
        val product = ProductDataRecord("itilnaya_tarelka", 239)
        `when`(servletRequest!!.getParameter("command")).thenReturn("max")
        `when`(productsModel!!.max()).thenReturn(Optional.of(product))

        runMockedServlet(
            queryServlet!!,
            "<html><body>" +
                    "<h1>The most expensive product:</h1>" +
                    "<br>${product.name}\t${product.price}</br>" +
                    "</body></html>"
        )
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testMin() {
        val product = ProductDataRecord("itilnaya_tarelka", 239)
        `when`(servletRequest!!.getParameter("command")).thenReturn("min")
        `when`(productsModel!!.min()).thenReturn(Optional.of(product))

        runMockedServlet(
            queryServlet!!,
            "<html><body>" +
                    "<h1>The least expensive product:</h1>" +
                    "<br>${product.name}\t${product.price}</br>" +
                    "</body></html>"
        )
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testCount() {
        val amount = 30
        `when`(servletRequest!!.getParameter("command")).thenReturn("count")
        `when`(productsModel!!.count()).thenReturn(amount)

        runMockedServlet(
            queryServlet!!,
            "<html><body>" +
                    "<h1>Amount of products:</h1>" +
                    "<br>$amount</br>" +
                    "</body></html>"
        )
    }

    @Test
    @Throws(IOException::class, SQLException::class)
    fun testSum() {
        val sum = 3333
        `when`(servletRequest!!.getParameter("command")).thenReturn("sum")
        `when`(productsModel!!.sum()).thenReturn(3333L)

        runMockedServlet(
            queryServlet!!,
            "<html><body>" +
                    "<h1>Summary price:</h1>" +
                    "<br>$sum</br>" +
                    "</body></html>"
        )
    }

    private fun runMockedServlet(testServlet: ProductsServlet, expectedResponse: String) {
        StringWriter().use { writer ->
            PrintWriter(writer).use { printer ->
                `when`(servletResponse!!.writer).thenReturn(printer)
                testServlet.doGet(servletRequest!!, servletResponse)
                printer.flush()
                assertThat("$writer").isEqualToIgnoringNewLines(expectedResponse)
            }
        }
    }
}
