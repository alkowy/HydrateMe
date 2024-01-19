package core.model

data class HydrationHubContentModel(
    val mythSections: List<MythSection>,
    val regularSections: List<RegularSection>,
)

data class MythSection(
    val mythContent: List<MythContent>,
    val title: String,
    val type: String,
)

data class MythContent(
    val fact: String,
    val myth: String,
)

data class RegularSection(
    val content: String,
    val title: String,
    val type: String,
)