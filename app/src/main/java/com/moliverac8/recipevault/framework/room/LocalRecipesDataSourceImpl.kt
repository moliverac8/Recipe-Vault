package com.moliverac8.recipevault.framework.room

import com.moliverac8.data.LocalRecipesDataSource
import javax.inject.Inject


class LocalRecipesDataSourceImpl @Inject constructor(val dao: LocalRecipeDatabaseDao) :
    LocalRecipesDataSource {

}