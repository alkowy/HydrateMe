package core.di

import core.input_validators.EmailInputValidator
import core.input_validators.InputValidator
import core.input_validators.PasswordInputValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
abstract class ValidatorModule {

    @Binds
    @Named("EmailValidator")
    abstract fun bindsEmailValidator(service: EmailInputValidator): InputValidator

    @Binds
    @Named("PasswordValidator")
    abstract fun bindsPasswordValidator(service: PasswordInputValidator): InputValidator
}