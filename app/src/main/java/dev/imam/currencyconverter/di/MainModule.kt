package dev.imam.currencyconverter.di

import android.content.Context
import android.content.SharedPreferences
import dev.imam.currencyconverter.data.mapper.CurrencyMapper
import dev.imam.currencyconverter.data.repository.CurrencyRepositoryImpl
import dev.imam.currencyconverter.data.repository.datasource.DataSource
import dev.imam.currencyconverter.data.repository.datasource.SharedPrefDataSource
import dev.imam.currencyconverter.domain.repository.CurrencyRepository
import dev.imam.currencyconverter.domain.usecase.AddCurrencyUseCase
import dev.imam.currencyconverter.domain.usecase.DeleteCurrencyUseCase
import dev.imam.currencyconverter.domain.usecase.GetCurrencyListUseCase
import dev.imam.currencyconverter.presentation.mapper.CurrencyModelMapper
import dev.imam.currencyconverter.presentation.view.fragment.CurrencyFragment
import dev.imam.currencyconverter.presentation.model.CurrencyService
import dev.imam.currencyconverter.presentation.viewmodel.CurrencyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mainModule = module {
    factory<SharedPreferences?> {
        androidContext().getSharedPreferences("test", Context.MODE_PRIVATE)
    }

    factory<DataSource> { SharedPrefDataSource(get()) }

    factory { CurrencyMapper() }

    factory { CurrencyModelMapper() }

    factory<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }

    factory { GetCurrencyListUseCase(get()) }

    factory { AddCurrencyUseCase(get()) }

    factory { DeleteCurrencyUseCase(get()) }

    viewModel() { CurrencyViewModel(get(), get(), get(), get()) }
}