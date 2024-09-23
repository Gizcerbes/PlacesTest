package com.uogames.balinasoft.test.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.uogames.balinasoft.core.usecase.auth.SignInUseCase
import com.uogames.balinasoft.core.usecase.auth.SignUpUseCase
import com.uogames.balinasoft.test.Config
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.FragmentAuthBinding
import com.uogames.balinasoft.test.ui.util.TabLayoutOnSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _bind: FragmentAuthBinding? = null
    private val bind get() = _bind!!

    private val vm by activityViewModels<AuthViewModule>()

    private var observerJob: Job? = null

    private var selected = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentAuthBinding.inflate(inflater, container, false)
        return _bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.tlTypes.apply { selectTab(getTabAt(vm.selectedTab.value)) }
        bind.tlTypes.addOnTabSelectedListener(TabLayoutOnSelectedListener(
            onSelected = { vm.selectedTab.value = it?.position ?: 0 }
        ))


        bind.tilLogin.editText?.setText(vm.login.value)
        bind.tilLogin.editText?.setSelection(vm.login.value.length)
        bind.tilLogin.editText?.doOnTextChanged { text, _, _, _ ->
            vm.login.value = text?.toString().orEmpty()
        }

        bind.tilPassword.editText?.setText(vm.password.value)
        bind.tilPassword.editText?.setSelection(vm.password.value.length)
        bind.tilPassword.editText?.doOnTextChanged { text, _, _, _ ->
            vm.password.value = text?.toString().orEmpty()
        }

        bind.tilPasswordRepeat.editText?.setText(vm.repeatPassword.value)
        bind.tilPasswordRepeat.editText?.setSelection(vm.repeatPassword.value.length)
        bind.tilPasswordRepeat.editText?.doOnTextChanged { text, _, _, _ ->
            vm.repeatPassword.value = text?.toString().orEmpty()
        }

        bind.mbRegister.setOnClickListener {
            when (vm.selectedTab.value) {
                1 -> vm.signUp()
                else -> vm.signIn()
            }
        }

    }

    override fun onStart() {
        observerJob = lifecycleScope.launch {
            vm.selectedTab.onEach {
                selected = it
                when (it) {
                    1 -> {
                        bind.tilPasswordRepeat.visibility = View.VISIBLE
                        bind.mbRegister.text = requireContext().getText(R.string.sign_up)
                        //bind.authNavHost.findNavController().navigate(R.id.registerFragment)
                    }

                    else -> {
                        bind.tilPasswordRepeat.visibility = View.GONE
                        bind.mbRegister.text = requireContext().getText(R.string.log_in)
                        //bind.authNavHost.findNavController().navigate(R.id.loginFragment)
                    }
                }

            }.launchIn(this)
            Config.accessToken.onEach {
                if (it != null)
                    requireActivity().findNavController(R.id.main_nav_host)
                        .navigate(R.id.rootFragment)
            }.launchIn(this)
            vm.busy.onEach {
                bind.rlLoading.visibility = if (it) View.VISIBLE else View.GONE
            }.launchIn(this)
            vm.errorHandler.onEach {
                when (it) {
                    is SignInUseCase.LoginRangeException, is SignUpUseCase.LoginRangeException -> {
                        vm.loginError.value = requireContext().getString(R.string.login_range_error)
                    }

                    is SignInUseCase.PasswordRangeException, is SignUpUseCase.PasswordRangeException -> {
                        vm.passwordError.value = requireContext().getString(R.string.password_range_error)
                    }

                    is SignInUseCase.SecuritySignIncorrectException -> {
                        vm.commonError.value = requireContext().getString(R.string.sign_error)
                    }

                    is SignInUseCase.LoginPatternException, is SignUpUseCase.LoginPatternException -> {
                        vm.loginError.value = requireContext().getString(R.string.login_pattern_error)
                        vm.commonError.value = requireContext().getString(R.string.login_common_error)
                    }

                    is SignUpUseCase.PasswordNotRepeat -> {
                        vm.repeatPasswordError.value = requireContext().getString(R.string.repeat_password_error)
                    }

                    is SignUpUseCase.SecuritySignupLoginAlreadyUse -> {
                        vm.login.value = requireContext().getString(R.string.signup_login_error)
                    }

                    else -> vm.commonError.value = it.message ?: requireContext().getString(R.string.unknown_error)
                }
            }.launchIn(this)
            vm.commonError.onEach {
                bind.mcvCommonError.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                bind.tvCommonError.text = it
            }.launchIn(this)
            vm.loginError.onEach {
                bind.tilLogin.error = it
            }.launchIn(this)
            vm.passwordError.onEach {
                bind.tilPassword.error = it
            }.launchIn(this)
            vm.repeatPasswordError.onEach {
                bind.tilPasswordRepeat.error = it
            }.launchIn(this)

        }
        super.onStart()
    }

    override fun onStop() {
        observerJob?.cancel()
        super.onStop()
    }


    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }


}