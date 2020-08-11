package com.arduia.expense.ui.common

import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.arduia.expense.R
import com.arduia.expense.ui.common.ExpenseCategory.Companion.CLOTHES
import com.arduia.expense.ui.common.ExpenseCategory.Companion.DONATION
import com.arduia.expense.ui.common.ExpenseCategory.Companion.EDUCATION
import com.arduia.expense.ui.common.ExpenseCategory.Companion.ENTERTAINMENT
import com.arduia.expense.ui.common.ExpenseCategory.Companion.FOOD
import com.arduia.expense.ui.common.ExpenseCategory.Companion.HEALTH_CARE
import com.arduia.expense.ui.common.ExpenseCategory.Companion.HOUSEHOLD
import com.arduia.expense.ui.common.ExpenseCategory.Companion.INCOME
import com.arduia.expense.ui.common.ExpenseCategory.Companion.OUTCOME
import com.arduia.expense.ui.common.ExpenseCategory.Companion.SOCIAL
import com.arduia.expense.ui.common.ExpenseCategory.Companion.TRANSPORTATION
import java.lang.Exception

class ExpenseCategoryProviderImpl(private val resource: Resources):
        ExpenseCategoryProvider{


    private val categoryList = mutableListOf<ExpenseCategory>()

    override fun getCategoryList(): List<ExpenseCategory> {
        return categoryList
    }

    override fun getCategoryDrawableByID(id: Int): Int {
        return getCategoryByID(id).img
    }

    init {
    categoryList.addAll(getCategoryData())

    }

    private fun getCategoryData() = mutableListOf<ExpenseCategory>().apply {
        add(ExpenseCategory(OUTCOME, (R.string.label_outcome).res(), R.drawable.ic_outcome ))
        add(ExpenseCategory(INCOME, (R.string.label_income).res(), R.drawable.ic_income))
        add(ExpenseCategory(FOOD, (R.string.label_food).res(), R.drawable.ic_food))
        add(ExpenseCategory(HOUSEHOLD, (R.string.label_household).res(), R.drawable.ic_household))
        add(ExpenseCategory(SOCIAL, (R.string.label_social).res(), R.drawable.ic_social))
        add(ExpenseCategory(ENTERTAINMENT, (R.string.label_entertainment).res(), R.drawable.ic_entertainment))
        add(ExpenseCategory(TRANSPORTATION, (R.string.label_transportation).res(), R.drawable.ic_transportation))
        add(ExpenseCategory(CLOTHES, (R.string.label_clothes).res(), R.drawable.ic_clothes))
        add(ExpenseCategory(HEALTH_CARE, (R.string.label_health_care).res(), R.drawable.ic_healthcare))
        add(ExpenseCategory(EDUCATION, (R.string.label_education).res(), R.drawable.ic_education))
        add(ExpenseCategory( DONATION, (R.string.label_donation).res(), R.drawable.ic_donation))
    }

    override fun getCategoryByID(id: Int): ExpenseCategory {
        return categoryList.firstOrNull{ it.id == id }?: throw Exception("Category Not Found $id")
    }

    override fun getIndexByCategory(category: ExpenseCategory): Int {
        return categoryList.indexOf(category)
    }

    private fun Int.res(): String = resource.getString(this)
}

data class ExpenseCategory(val id: Int,
                           val name: String,
                           @DrawableRes val img: Int){
    companion object{
        const val OUTCOME = 1
        const val INCOME = 2
        const val FOOD = 3
        const val HOUSEHOLD = 4
        const val SOCIAL = 5
        const val ENTERTAINMENT = 6
        const val TRANSPORTATION = 7
        const val CLOTHES = 8
        const val HEALTH_CARE = 9
        const val EDUCATION = 10
        const val DONATION = 11
    }
}