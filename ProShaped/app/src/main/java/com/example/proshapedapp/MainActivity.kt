package com.example.proshapedapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proshapedapp.ui.theme.ProShapedAppTheme
import kotlinx.coroutines.delay
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.NoFood
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import com.example.proshapedapp.caloriesScreenPackage.GetRandomPhoto
import com.example.proshapedapp.caloriesScreenPackage.ImageCard
import com.example.proshapedapp.caloriesScreenPackage.PhotoData
import com.example.proshapedapp.caloriesScreenPackage.PhotosData
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProShapedAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Macros",
                                    route = "macros",
                                    icon = Icons.Filled.FoodBank
                                ),
                                BottomNavItem(
                                    name = "Calories",
                                    route = "calories",
                                    icon = Icons.Filled.NoFood
                                ),
                                BottomNavItem(
                                    name = "Settings",
                                    route = "settings",
                                    icon = Icons.Default.Settings
                                ),
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("macros") {
            MacrosScreen()
        }
        composable("calories") {
            CaloriesScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Cyan,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        if(item.badgeCount > 0) {
                            BadgeBox(
                                badgeContent = {
                                    Text(text = item.badgeCount.toString())
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if(selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MacrosScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Macros screen")
    }
}

//Composable for the calories screen
//  !!!  Feature done   !!!
@SuppressLint("UnrememberedMutableState")
@Composable
fun CaloriesScreen() {
    val scope = rememberCoroutineScope()
    val photosList = PhotosData.getData()
    var photoData = mutableStateOf(GetRandomPhoto(photosList).randomPhoto())

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //here we will create an image card

        var painter = painterResource(id = photoData.value.imageResourceId)
        var description = photoData.value.calories
        var title = photoData.value.calories

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            ImageCard(
                painter = painter,
                contentDescription = description,
                title = title
            )
        }
        Button(onClick = {
            scope.launch {
                photoData.value = GetRandomPhoto(photosList).randomPhoto()
            }
        },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.LightGray

            ),
            modifier = Modifier
                .border(
                    width = 5.dp,
                    brush = Brush.horizontalGradient(listOf(Color.Cyan, Color.Blue)),
                    shape = RoundedCornerShape(15.dp)
                )
                .width(100.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent
                        ),
                        startX = 150f
                    )
                )
        ) {
            Text(text = "Next")
        }

    }

}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings screen")
    }
}

@Composable
fun SplashScreen(navController: NavHostController){
    val scale = remember{
        Animatable(0f)

    }
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        navController.navigate("macros")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.capybara),
//            contentDescription = "Logo",
//            modifier = Modifier.scale(scale.value)
//        )
    }
}