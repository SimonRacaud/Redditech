package my.epi.redditech.model.api

data class ImageModel(
    val id: String,
    val resolutions: List<ResolutionModel>,
    val source: ResolutionModel
)