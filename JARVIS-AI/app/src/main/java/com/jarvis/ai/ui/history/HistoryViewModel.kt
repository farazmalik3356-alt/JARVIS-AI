package com.jarvis.ai.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.data.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    val conversations: StateFlow<List<ConversationEntity>> =
        conversationRepository.observeConversations()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val selectedMessages: StateFlow<List<MessageEntity>> = _selectedMessages.asStateFlow()

    private val _selectedId = MutableStateFlow<String?>(null)
    val selectedId: StateFlow<String?> = _selectedId.asStateFlow()

    private var messagesJob: kotlinx.coroutines.Job? = null

    fun selectConversation(id: String) {
        _selectedId.value = id
        messagesJob?.cancel()
        messagesJob = viewModelScope.launch {
            conversationRepository.observeMessages(id).collect {
                _selectedMessages.value = it
            }
        }
    }

    fun closeDetail() {
        messagesJob?.cancel()
        _selectedId.value = null
        _selectedMessages.value = emptyList()
    }

    fun delete(id: String) = viewModelScope.launch {
        conversationRepository.deleteConversation(id)
        if (_selectedId.value == id) closeDetail()
    }

    fun clearAll() = viewModelScope.launch {
        conversationRepository.clearAll()
        closeDetail()
    }
}