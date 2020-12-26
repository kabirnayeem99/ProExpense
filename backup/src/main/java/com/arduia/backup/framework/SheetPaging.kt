package com.arduia.backup.framework

import java.lang.Exception
import java.lang.IllegalArgumentException

abstract class SheetPaging (val pagination: Int){


    abstract fun provideName(pageIndex: Int): String

    init {
        if(0 >= this.pagination) throw IllegalArgumentException("pagination must be grather than zero!")
    }

}
