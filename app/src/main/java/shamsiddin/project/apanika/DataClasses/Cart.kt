package shamsiddin.project.apanika.DataClasses

data class Cart(
    val discountedTotal: Int,
    val id: Int,
    val products: List<ProductX>,
    val total: Int,
    val totalProducts: Int,
    val totalQuantity: Int,
    val userId: Int
)
