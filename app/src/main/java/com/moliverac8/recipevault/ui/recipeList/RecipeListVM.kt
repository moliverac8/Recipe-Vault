package com.moliverac8.recipevault.ui.recipeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moliverac8.domain.RecipeWithIng
import com.moliverac8.recipevault.toListOfString
import com.moliverac8.usecases.DeleteRecipe
import com.moliverac8.usecases.GetAllRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipeListVM @Inject constructor(
    private val getRecipes: GetAllRecipes,
    private val deleteRecipe: DeleteRecipe
) : ViewModel() {

    private val _recipes = MutableLiveData<List<RecipeWithIng>>()
    val recipes: LiveData<List<RecipeWithIng>>
        get() = _recipes

    private var originalRecipes = listOf<RecipeWithIng>()

    fun loadOriginalRecipes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _recipes.postValue(originalRecipes)
            }
        }
    }

    fun updateRecipes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                originalRecipes = getRecipes()
                _recipes.postValue(originalRecipes)
            }
        }
    }

    fun deleteRecipeOnDatabase(recipe: RecipeWithIng) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRecipe(recipe)
            }
        }
    }

    fun removeRecipeFromObservable(recipe: RecipeWithIng) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _recipes.postValue(recipes.value?.toMutableList()?.apply {
                    remove(recipe)
                })
            }
        }
    }

    fun addRecipeFromObservable(recipe: RecipeWithIng) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _recipes.postValue(recipes.value?.toMutableList()?.apply {
                    add(recipe)
                })
            }
        }
    }

    fun filterByChips(filters: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val recipes = originalRecipes
                val filteredRecipes = recipes.filter { recipe ->
                    filters.any { it == recipe.domainRecipe.dietType.name } ||
                            filters.any {
                                recipe.domainRecipe.dishType.toListOfString().contains(it)
                            }
                }
                _recipes.postValue(filteredRecipes)
            }
        }
    }
}