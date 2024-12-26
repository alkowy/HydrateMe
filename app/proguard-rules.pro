-keep public class com.google.firebase.** {*;}
-keep class com.google.android.gms.internal.** {*;}

# Keep all classes defined in HydrationHubContentModel.kt
-keep class core.model.HydrationHubContentModel { *; }
-keep class core.model.DehydrationSignsSection { *; }
-keep class core.model.HydrationBenefitsSection { *; }
-keep class core.model.HydrationTipsSection { *; }
-keep class core.model.MythSection { *; }
-keep class core.model.TipContent { *; }
-keep class core.model.MythContent { *; }
-keep class core.model.HydrationBenefitContent { *; }
-keep class core.model.DehydrationSignContent { *; }
