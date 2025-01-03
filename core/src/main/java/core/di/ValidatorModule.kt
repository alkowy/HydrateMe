package core.di

import core.input_validators.DateValidator
import core.input_validators.EmailInputValidator
import core.input_validators.HeightValidator
import core.input_validators.InputValidator
import core.input_validators.PasswordInputValidator
import core.input_validators.DecimalNumberValidator
import core.input_validators.WholeNumberValidator
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

    @Binds
    @Named("DecimalValidator")
    abstract fun bindsWeightValidator(service: DecimalNumberValidator): InputValidator

    @Binds
    @Named("HeightValidator")
    abstract fun bindsHeightValidator(service: HeightValidator): InputValidator

    @Binds
    @Named("DateValidator")
    abstract fun bindsDateValidator(service: DateValidator): InputValidator

    @Binds
    @Named("WholeNumberValidator")
    abstract fun bindsWholeNumberValidator(service: WholeNumberValidator): InputValidator
}