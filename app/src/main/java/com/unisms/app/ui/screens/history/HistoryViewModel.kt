package com.unisms.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unisms.app.data.local.db.ActivationRecordEntity
import com.unisms.app.data.model.ActivationFilter
import com.unisms.app.data.repository.SmsBowerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: SmsBowerRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(ActivationFilter.ALL)
    val filter: StateFlow<ActivationFilter> = _filter

    val historyRecords: StateFlow<List<ActivationRecordEntity>> =
        combine(repository.getHistory(), _filter) { records, currentFilter ->
            when (currentFilter) {
                ActivationFilter.ALL -> records
                ActivationFilter.ACTIVE -> records.filter { it.status == "ACTIVE" }
                ActivationFilter.COMPLETED -> records.filter { it.status == "COMPLETED" }
                ActivationFilter.CANCELLED -> records.filter { it.status == "CANCELLED" || it.status == "TIMEOUT" }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(newFilter: ActivationFilter) {
        _filter.value = newFilter
    }

    fun deleteRecord(record: ActivationRecordEntity) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}
