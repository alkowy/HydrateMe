package core.model

data class HydrationHubContentModel(
    val dehydrationSignsSection: DehydrationSignsSection,
    val hydrationBenefitsSection: HydrationBenefitsSection,
    val hydrationTipsSection: HydrationTipsSection,
    val mythSection: MythSection,
)

data class TipContent(
    val generalTip: String,
    val tipExplanation: String,
)

data class MythSection(
    val mythContent: List<MythContent>,
    val title: String,
    val type: String,
)

data class MythContent(
    val myth: String,
    val fact: String,
)

data class HydrationTipsSection(
    val tipContent: List<TipContent>,
    val title: String,
    val type: String,
)

data class HydrationBenefitsSection(
    val hydrationBenefitContent: List<HydrationBenefitContent>,
    val title: String,
    val type: String,
)

data class HydrationBenefitContent(
    val benefitExplanation: String,
    val generalBenefit: String,
)

data class DehydrationSignsSection(
    val dehydrationSignContent: List<DehydrationSignContent>,
    val title: String,
    val type: String,
)

data class DehydrationSignContent(
    val generalSign: String,
    val signExplanation: String,
)