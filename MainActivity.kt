package com.guregram.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TgBlue = Color(0xFF3390EC)
private val Bg = Color(0xFFFFFFFF)
private val TextMain = Color(0xFF17212B)
private val TextMuted = Color(0xFF7D8B99)
private val DividerColor = Color(0xFFE9EDF0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GuregramTelegramStyle() }
    }
}

private data class Chat(
    val title: String,
    val preview: String,
    val time: String,
    val avatarColor: Color,
    val avatarText: String,
    val unread: Int = 0,
    val verified: Boolean = false,
    val folder: String = "Все чаты",
    val pinned: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuregramTelegramStyle() {
    var currentTab by remember { mutableStateOf("Чаты") }
    var searchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var drawerOpen by remember { mutableStateOf(false) }
    var settingsOpen by remember { mutableStateOf(false) }
    var selectedChat by remember { mutableStateOf<Chat?>(null) }
    var selectedFolder by remember { mutableStateOf("Все") }
    var notifications by remember { mutableStateOf(true) }
    var saveToGallery by remember { mutableStateOf(false) }
    var autoDownload by remember { mutableStateOf(true) }

    val chats = listOf(
        Chat("Saved Messages", "Сохранённые сообщения", "19:42", Color(0xFF7E57C2), "★", pinned = true),
        Chat("Telegram", "Login code: ••••• (демо)", "19:36", Color(0xFF3390EC), "✈", verified = true, unread = 1, pinned = true),
        Chat("PUBG CUSTOM", "Матч сегодня в 20:00", "19:18", Color(0xFF43A047), "P", unread = 5, folder = "Группы"),
        Chat("Друзья", "Окей, договорились 🤝", "18:54", Color(0xFFE58A3A), "Д", unread = 2, folder = "Личные"),
        Chat("GUREGRAM Updates", "Добро пожаловать в GUREGRAM", "18:20", Color(0xFF3390EC), "G", verified = true, folder = "Каналы"),
        Chat("Minecraft Server", "Сервер снова онлайн", "Вчера", Color(0xFF4CAF50), "M", folder = "Группы"),
        Chat("Мама", "Не забудь написать", "Вчера", Color(0xFFE57373), "М", folder = "Личные")
    )

    val filtered = chats.filter {
        (selectedFolder == "Все" || it.folder == selectedFolder || (selectedFolder == "Все" && it.pinned)) &&
        (searchText.isBlank() || it.title.contains(searchText, ignoreCase = true) || it.preview.contains(searchText, ignoreCase = true))
    }

    MaterialTheme(colorScheme = lightColorScheme(primary = TgBlue, background = Bg, surface = Bg)) {
        Scaffold(
            containerColor = Bg,
            topBar = {
                TopAppBar(
                    title = {
                        if (searchMode) {
                            TextField(
                                value = searchText, onValueChange = { searchText = it },
                                placeholder = { Text("Поиск", color = TextMuted) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            Text(
                                when {
                                    settingsOpen -> "Настройки"
                                    currentTab == "Контакты" -> "Контакты"
                                    currentTab == "Звонки" -> "Звонки"
                                    else -> "GUREGRAM"
                                },
                                color = TextMain, fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (settingsOpen) settingsOpen = false
                            else if (searchMode) { searchMode = false; searchText = "" }
                            else drawerOpen = true
                        }) {
                            Icon(if (searchMode || settingsOpen) Icons.Default.Menu else Icons.Default.Menu, contentDescription = "Меню", tint = TextMuted)
                        }
                    },
                    actions = {
                        if (!settingsOpen) {
                            IconButton(onClick = { searchMode = !searchMode; if (!searchMode) searchText = "" }) {
                                Icon(Icons.Default.Search, contentDescription = "Поиск", tint = TextMuted)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Bg)
                )
            },
            floatingActionButton = {
                if (!settingsOpen && currentTab == "Чаты") {
                    FloatingActionButton(onClick = { selectedChat = Chat("Новое сообщение", "Демо-экран создания чата", "", TgBlue, "✎") }, containerColor = TgBlue, contentColor = Color.White) {
                        Icon(Icons.Default.Edit, contentDescription = "Новое сообщение")
                    }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Bg, contentColor = TgBlue, tonalElevation = 1.dp) {
                    NavigationBarItem(selected = currentTab == "Контакты", onClick = { currentTab = "Контакты"; settingsOpen = false }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Контакты", fontSize = 10.sp) })
                    NavigationBarItem(selected = currentTab == "Звонки", onClick = { currentTab = "Звонки"; settingsOpen = false }, icon = { Icon(Icons.Default.Call, null) }, label = { Text("Звонки", fontSize = 10.sp) })
                    NavigationBarItem(selected = currentTab == "Чаты", onClick = { currentTab = "Чаты"; settingsOpen = false }, icon = { Icon(Icons.Default.Chat, null) }, label = { Text("Чаты", fontSize = 10.sp) })
                    NavigationBarItem(selected = settingsOpen, onClick = { settingsOpen = true }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Настройки", fontSize = 10.sp) })
                }
            }
        ) { padding ->
            if (settingsOpen) {
                SettingsContent(
                    Modifier.padding(padding),
                    notifications, { notifications = it },
                    autoDownload, { autoDownload = it },
                    saveToGallery, { saveToGallery = it }
                )
            } else if (currentTab == "Контакты") {
                EmptyTab(Modifier.padding(padding), "Контакты", "Контакты появятся после подключения аккаунта Telegram.")
            } else if (currentTab == "Звонки") {
                EmptyTab(Modifier.padding(padding), "Звонки", "История звонков появится после интеграции Telegram.")
            } else {
                Column(Modifier.padding(padding)) {
                    ScrollableTabRow(
                        selectedTabIndex = listOf("Все", "Личные", "Группы", "Каналы").indexOf(selectedFolder).coerceAtLeast(0),
                        containerColor = Bg, contentColor = TgBlue, edgePadding = 8.dp,
                        divider = {}
                    ) {
                        listOf("Все", "Личные", "Группы", "Каналы").forEach { folder ->
                            Tab(
                                selected = selectedFolder == folder,
                                onClick = { selectedFolder = folder },
                                text = { Text(folder, fontSize = 14.sp, maxLines = 1) }
                            )
                        }
                    }
                    if (filtered.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ничего не найдено", color = TextMuted)
                        }
                    } else {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(filtered) { chat ->
                                ChatRow(chat) { selectedChat = chat }
                            }
                        }
                    }
                }
            }
        }

        if (drawerOpen) {
            ModalNavigationDrawer(
                drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
                gesturesEnabled = true,
                drawerContent = {
                    ModalDrawerSheet(drawerContainerColor = Bg) {
                        Column(Modifier.fillMaxWidth().background(TgBlue).padding(20.dp)) {
                            Box(Modifier.size(58.dp).background(Color.White.copy(alpha = .2f), CircleShape), contentAlignment = Alignment.Center) {
                                Text("G", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("GUREGRAM", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("Неофициальный UI-прототип", color = Color.White.copy(alpha = .85f), fontSize = 12.sp)
                        }
                        NavigationDrawerItem(label = { Text("Все чаты") }, selected = true, onClick = { selectedFolder = "Все"; drawerOpen = false }, icon = { Icon(Icons.Default.Chat, null) })
                        NavigationDrawerItem(label = { Text("Архив") }, selected = false, onClick = { selectedFolder = "Все"; drawerOpen = false }, icon = { Icon(Icons.Default.Archive, null) })
                        NavigationDrawerItem(label = { Text("Настройки") }, selected = false, onClick = { settingsOpen = true; drawerOpen = false }, icon = { Icon(Icons.Default.Settings, null) })
                        NavigationDrawerItem(label = { Text("Закрыть меню") }, selected = false, onClick = { drawerOpen = false }, icon = { Icon(Icons.Default.Menu, null) })
                    }
                }
            ) {
                // Drawer is a visual prototype; main content remains underneath.
            }
        }

        selectedChat?.let { chat ->
            AlertDialog(
                onDismissRequest = { selectedChat = null },
                title = { Text(chat.title) },
                text = { Text("Это демонстрационный экран GUREGRAM. Реальные сообщения, авторизация и медиа ещё не подключены.") },
                confirmButton = { TextButton(onClick = { selectedChat = null }) { Text("Назад") } },
                containerColor = Bg
            )
        }
    }
}

@Composable
private fun ChatRow(chat: Chat, onClick: () -> Unit) {
    Column {
        Row(
            Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(54.dp).background(chat.avatarColor, CircleShape), contentAlignment = Alignment.Center) {
                Text(chat.avatarText, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(chat.title, color = TextMain, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (chat.verified) {
                        Spacer(Modifier.width(4.dp))
                        Text("✓", color = TgBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(chat.preview, color = TextMuted, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                Text(chat.time, color = if (chat.unread > 0) TgBlue else TextMuted, fontSize = 11.sp)
                if (chat.unread > 0) {
                    Spacer(Modifier.height(5.dp))
                    Box(Modifier.background(TgBlue, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(chat.unread.toString(), color = Color.White, fontSize = 11.sp)
                    }
                } else if (chat.pinned) {
                    Spacer(Modifier.height(5.dp))
                    Text("⌁", color = TextMuted, fontSize = 15.sp)
                }
            }
        }
        Spacer(Modifier.padding(start = 80.dp).fillMaxWidth().height(0.6.dp).background(DividerColor))
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier,
    notifications: Boolean, onNotifications: (Boolean) -> Unit,
    autoDownload: Boolean, onAutoDownload: (Boolean) -> Unit,
    saveToGallery: Boolean, onSaveToGallery: (Boolean) -> Unit
) {
    Column(modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text("GUREGRAM", color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Настройки приложения", color = TextMuted, fontSize = 14.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text("УВЕДОМЛЕНИЯ И ЗВУКИ", color = TgBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        PrefRow("Уведомления", "Демо-переключатель уведомлений", notifications, onNotifications)
        Text("ДАННЫЕ И ПАМЯТЬ", color = TgBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        PrefRow("Автозагрузка медиа", "Автоматическая загрузка фото и видео (демо)", autoDownload, onAutoDownload)
        PrefRow("Сохранять в галерею", "Демо-переключатель сохранения медиа", saveToGallery, onSaveToGallery)
        Spacer(Modifier.height(18.dp))
        Text("Версия 0.2.0 • интерфейсный прототип", color = TextMuted, fontSize = 12.sp)
        Text("Пока использует демонстрационные данные и не подключён к серверам Telegram.", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun PrefRow(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = TextMain, fontSize = 16.sp)
            Text(subtitle, color = TextMuted, fontSize = 12.sp)
        }
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}

@Composable
private fun EmptyTab(modifier: Modifier, title: String, body: String) {
    Box(modifier.fillMaxSize().padding(28.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(body, color = TextMuted, fontSize = 14.sp)
        }
    }
}
