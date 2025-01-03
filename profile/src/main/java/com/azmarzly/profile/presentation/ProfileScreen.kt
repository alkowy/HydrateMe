package com.azmarzly.profile.presentation

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.azmarzly.core.R.drawable
import com.azmarzly.core.R.string
import com.azmarzly.profile.R
import core.ui.theme.HydrateMeTheme
import core.ui.theme.backgroundContainer
import core.ui.theme.bodySmall
import core.ui.theme.profileEmailTextColor
import core.ui.theme.shadowedTextColor
import core.util.clickableOnce

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    bottomBarPadding: Dp,
    navigateToSettings: () -> Unit,
) {
    val state by profileViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("ANANAS", "ProfileScreen: LAUNCHED EFFECT UNIT")
        profileViewModel.initialiseProfileState()
    }

    ProfileScreenContent(
        state = state,
        bottomBarPadding = bottomBarPadding,
        uploadProfilePicture = profileViewModel::updateProfilePicture,
        navigateToSettings = navigateToSettings,
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    bottomBarPadding: Dp,
    uploadProfilePicture: (Uri) -> Unit,
    navigateToSettings: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = bottomBarPadding)
    ) {

        ProfileHeader(
            navigateToSettings = navigateToSettings
        )
        Spacer(modifier = Modifier.height(24.dp))
        MainProfileCard(
            state = state,
            uploadProfilePicture = uploadProfilePicture,
        )
        Spacer(modifier = Modifier.height(14.dp))
        ProfileOneParameterSectionCard(
            parameterName = stringResource(string.physical_activity),
            parameterValue = state.activity
        )
        Spacer(modifier = Modifier.height(14.dp))
        ProfileOneParameterSectionCard(
            parameterName = stringResource(string.daily_water_goal),
            parameterValue = state.dailyGoal
        )
        Spacer(modifier = Modifier.height(14.dp))
        HydrationRemindersRow(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun HydrationRemindersRow(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val channelId = "reminders_channel"
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    var isChannelEnabled by remember { mutableStateOf(isNotificationChannelEnabled(context, channelId)) }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isChannelEnabled = isNotificationChannelEnabled(context, channelId)
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = stringResource(string.hydration_reminders))
        Icon(
            modifier = Modifier
                .clickable { openAppSettings(context = context) }
                .size(36.dp),
            imageVector = (if (isChannelEnabled) Icons.Filled.NotificationsActive else Icons.Filled.NotificationsOff),
            contentDescription = null,
        )
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun isNotificationChannelEnabled(context: Context, channelId: String): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (notificationManager.areNotificationsEnabled().not()) {
        return false
    }
    val channel = notificationManager.getNotificationChannel(channelId)
    if (channel != null) {
        return channel.importance != NotificationManager.IMPORTANCE_NONE &&
                NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
    return false
}

@Composable
fun ProfileOneParameterSectionCard(parameterName: String, parameterValue: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.background
        )
    ) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = parameterName,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colors.shadowedTextColor
                )
            )

            Text(
                text = parameterValue,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )
        }
    }
}

@Composable
private fun MainProfileCard(
    state: ProfileUiState,
    uploadProfilePicture: (Uri) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.background
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth(),
        ) {

            ProfilePicture(
                profilePictureUrl = state.profileImageUrl,
                uploadProfilePicture = uploadProfilePicture,
                useLocalImageFromUri = state.useLocalImageFromUri,
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.name,
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.onBackground
                )
            )

            Text(
                text = state.email,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colors.profileEmailTextColor
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileParameterItem(parameterName = stringResource(string.parameters_age), parameterValue = state.age)
                ProfileParameterItem(parameterName = stringResource(string.parameters_weight), parameterValue = state.weight)
                ProfileParameterItem(parameterName = stringResource(string.parameters_height), parameterValue = state.height)
            }
        }
    }
}

@Composable
fun RowScope.ProfileParameterItem(parameterName: String, parameterValue: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = parameterName,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colors.shadowedTextColor
            )
        )
        Text(
            text = parameterValue,
            style = MaterialTheme.typography.bodySmall.copy(
                MaterialTheme.colors.onBackground
            )
        )
    }
}

@Composable
private fun ProfilePicture(
    profilePictureUrl: String,
    useLocalImageFromUri: Boolean,
    uploadProfilePicture: (Uri) -> Unit,
) {

    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            Log.d("ANANAS", "ProfilePicture: uri $uri")
            uri?.let {
                selectedImageUri = uri
                uploadProfilePicture(uri)
            }
        }
    )

    Box {
        if (useLocalImageFromUri) {
            AsyncImage(
                model = selectedImageUri,
                error = painterResource(drawable.ic_profile_selected),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(140.dp)
                    .align(Alignment.Center)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(profilePictureUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(drawable.ic_profile_selected),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(140.dp)
                    .align(Alignment.Center)
            )
        }

        IconButton(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(20.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.backgroundContainer),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit profile picture",
                tint = MaterialTheme.colors.onBackground,
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    navigateToSettings: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(string.profile_header),
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.onBackground
            )
        )
        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                modifier = Modifier
                    .clickableOnce {
                        navigateToSettings()
                    },
                imageVector = Icons.Outlined.Settings,
                contentDescription = "settings"
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun ProfileContentPreview() {
    HydrateMeTheme {
        ProfileScreenContent(
            bottomBarPadding = 20.dp,
            state = ProfileUiState(
                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
                false,
                "OLO",
                "olo123123123@gmail.com",
                "44",
                "80 kg",
                "180 cm",
                "EMPTY",
                "2.2 L"
            ),
            uploadProfilePicture = {},
            navigateToSettings = {},
        )
    }
}