package net.cocooncreations.countriesmvvm.di.component

import dagger.Component
import net.cocooncreations.countriesmvvm.di.modules.ApiModule
import net.cocooncreations.countriesmvvm.model.CountriesService
import net.cocooncreations.countriesmvvm.viewmodel.ListViewModel

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)

    fun inject(viewModel:ListViewModel)

}