package xml.one.pass.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Learn more about inline functions and what is reified?
 */
inline fun <reified T> Flow<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    lifecycleOwner.repeatOnLifecycle(lifecycleState) {
        collect(action)
    }
}

inline fun <reified T> Flow<T>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState).collect(action)
}
