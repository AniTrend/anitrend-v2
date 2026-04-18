package co.anitrend.profile.component.model

internal sealed interface ProfileSectionState<out T> {
    val state: T?

    data object Loading : ProfileSectionState<Nothing> {
        override val state: Nothing? = null
    }

    data object Empty : ProfileSectionState<Nothing> {
        override val state: Nothing? = null
    }

    data class Error(
        val cause: Throwable? = null,
    ) : ProfileSectionState<Nothing> {
        override val state: Nothing? = null
    }

    data class Partial<T>(
        val data: T,
        val cause: Throwable? = null,
    ) : ProfileSectionState<T> {
        override val state: T = data
    }

    data class Content<T>(
        val data: T,
    ) : ProfileSectionState<T> {
        override val state: T = data
    }
}
