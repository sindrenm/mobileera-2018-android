package rocks.mobileera.mobileera.model

import rocks.mobileera.mobileera.R


class Tag {

    companion object {
        fun background(name: String): Int {
            when (name) {

                "Android" -> return R.drawable.selector_background_tag_android
                "iOS" -> return R.drawable.selector_background_tag_ios
                "Cross-platform" -> return R.drawable.selector_background_tag_cross_platform
                "Productivity" -> return R.drawable.selector_background_tag_productivity
                "Mobile Web" -> return R.drawable.selector_background_tag_mobile_web
                "Security" -> return R.drawable.selector_background_tag_security
                "IoT" -> return R.drawable.selector_background_tag_iot
                "AI" -> return R.drawable.selector_background_tag_ai
                "Machine Learning" -> return R.drawable.selector_background_tag_machine_learning
                "Architecture" -> return R.drawable.selector_background_tag_architecture
                "Backend" -> return R.drawable.selector_background_tag_backend
                "CI" -> return R.drawable.selector_background_tag_ci
                "React Native" -> return R.drawable.selector_background_tag_react_native
            }

            return R.drawable.selector_background_tag_android
        }
    }
}