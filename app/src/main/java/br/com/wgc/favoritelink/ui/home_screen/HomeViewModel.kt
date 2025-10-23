package br.com.wgc.favoritelink.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.mapper.toItemLinkModel
import br.com.wgc.favoritelink.data.mapper.toLinkEntity
import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import br.com.wgc.favoritelink.data.repository.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<HomeScreenState> = combine(
        linkRepository.getAllLinks(),
        _searchText,
        _errorMessage
    ) { links, text, error ->
        val filteredLinks = entitiesFilter(text, links).map { it.toItemLinkModel() }

        HomeScreenState(
            links = filteredLinks,
            searchText = text,
            isLoading = false,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeScreenState(isLoading = true)
    )

    private fun entitiesFilter(
        text: String,
        links: List<LinkEntity>
    ): List<LinkEntity> = if (text.isBlank()) links
    else links.filter {
        it.name.contains(text, ignoreCase = true)
                || it.url.contains(text, ignoreCase = true)
    }


    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }

    fun onAddLinkClick(name: String, url: String) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                linkRepository.createAndSaveLink(name, CreateAliasRequest(url))
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao criar o link: ${e.message}"
            }
        }
    }

    fun onFavoriteClick(id: UUID) {
        viewModelScope.launch {
            val linkToUpdate = uiState.value.links.find { it.id == id }?.toLinkEntity()
            linkToUpdate?.let {
                val updated = it.copy(isFavorite = !it.isFavorite)
                try {
                    linkRepository.updateLink(updated)
                } catch (e: Exception) {
                    _errorMessage.value = "Falha ao atualizar o link: ${e.message}"
                }
            }
        }
    }

    fun getLinkByID(id: UUID) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                linkRepository.getLinkByID(id.toString())
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao criar o link: ${e.message}"
            }
        }
    }

    fun deleteLink(id: UUID) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                linkRepository.deleteLink(id)
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao criar o link: ${e.message}"
            }
        }
    }
}