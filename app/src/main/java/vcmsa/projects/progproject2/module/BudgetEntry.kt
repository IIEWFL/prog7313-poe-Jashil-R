package vcmsa.projects.progproject2.module

data class BudgetEntry(
    val id: String = "",               // Firebase unique key
    val category: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val date: Long = 0L,
    val goalMin: Double = 0.0,
    val goalMax: Double = 0.0
)