package xyz.codegeek.outbreakvisualizer.Di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import xyz.codegeek.outbreakvisualizer.viewmodels.HeatmapViewModel
import xyz.codegeek.outbreakvisualizer.viewmodels.TrendsViewModel

val uiModule = module {
    viewModel { HeatmapViewModel(get()) }
    viewModel { TrendsViewModel(get()) }
}