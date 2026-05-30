<div align="center">

<img src="app/src/main/res/drawable/ic_launcher_foreground.xml" width="80" alt="ChamaZero Logo" />

# 🔥 ChamaZero

### Monitoramento inteligente de terrenos para prevenção de incêndios

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange?style=flat-square)](https://developer.android.com/topic/architecture)
[![API](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-green?style=flat-square)](https://developer.android.com/about/versions/oreo)

</div>

---

## 📋 Sobre o Projeto

O **ChamaZero** é um aplicativo Android nativo desenvolvido como projeto de **Global Solution** da disciplina de Engenharia de Software da **FIAP**, turma **3ESOA** — 2026.

O tema escolhido para esta edição da Global Solution foi a **utilização de Inteligência Artificial para monitorar, prevenir e reduzir incêndios em terrenos rurais**. O ChamaZero representa a camada mobile dessa solução, permitindo que produtores rurais acompanhem em tempo real as condições climáticas de seus terrenos, recebam alertas de risco de incêndio e gerenciem sistemas de irrigação inteligente.

---

## 🎯 Problema & Solução

### O Problema
Incêndios em terrenos rurais causam prejuízos bilionários ao agronegócio brasileiro e impactos ambientais irreversíveis. A falta de monitoramento contínuo e a demora na detecção de condições de risco são os principais fatores que transformam focos controláveis em grandes incêndios.

### A Solução
O ChamaZero integra dados de sensores IoT instalados nos terrenos (temperatura, umidade do ar e pressão atmosférica) com um modelo de IA que calcula em tempo real o **índice de risco de incêndio** de cada propriedade. Com base nesses dados, o sistema pode acionar automaticamente sistemas de irrigação preventiva, notificar o proprietário e sugerir ações corretivas antes que o fogo se inicie.

---

## 👥 Equipe

| Nome | RM |
|------|----|
| [Nome do Aluno 1] | RM XXXXX |
| [Nome do Aluno 2] | RM XXXXX |
| [Nome do Aluno 3] | RM XXXXX |

> **Instituição:** FIAP — Faculdade de Informática e Administração Paulista
> **Curso:** Engenharia de Software
> **Turma:** 3ESOA
> **Ano:** 2026

---

## 📱 Funcionalidades

### Autenticação
- ✅ Login com e-mail e senha
- ✅ Cadastro de nova conta com validação de campos
- ✅ Persistência de sessão entre aberturas do app
- ✅ Logout seguro

### Terrenos
- ✅ Listagem de todos os terrenos cadastrados
- ✅ Cadastro de novo terreno com dados completos de endereço e localização
- ✅ Edição de terrenos existentes
- ✅ Visualização de dados climáticos em tempo real (temperatura, umidade, pressão)
- ✅ Indicador visual de risco de incêndio com barra de progresso colorida
- ✅ Status do sistema de irrigação (ativo/inativo)
- ✅ Data da última irrigação

### Perfil
- ✅ Visualização dos dados do usuário logado
- ✅ Edição de nome e e-mail
- ✅ Avatar com iniciais do nome

### UX / Interface
- ✅ Design system consistente com paleta verde (tema agro)
- ✅ Bottom Navigation Bar com 3 abas (Início, Terreno, Perfil)
- ✅ Suporte a edge-to-edge com respeito à status bar nativa
- ✅ Validação de campos com limites de caracteres e tipos de entrada
- ✅ Feedback visual de loading e erros via Snackbar

---

## 🏗️ Arquitetura

O projeto segue a arquitetura **MVVM (Model-View-ViewModel)** com separação clara de responsabilidades em camadas:

```
app/
└── src/main/java/br/com/chamazero/
    ├── data/
    │   ├── model/          # Data classes (entidades de domínio)
    │   ├── remote/         # Retrofit, MockInterceptor, MockDataStore
    │   └── repository/     # Repositórios (ponte entre ViewModel e API)
    ├── ui/
    │   ├── component/      # Componentes reutilizáveis (AppButton, AppTextField, etc.)
    │   ├── navigation/     # NavGraph e rotas
    │   ├── screen/         # Telas (Login, Register, Home, AddTerrain, EditTerrain, Profile, EditProfile)
    │   ├── theme/          # Cores, tipografia e tema Material3
    │   └── viewmodel/      # ViewModels (AuthViewModel, TerrainViewModel, ProfileViewModel)
    ├── util/               # Classes utilitárias (Resource sealed class)
    ├── ChamaZeroApp.kt     # Application class
    └── MainActivity.kt     # Entry point
```

### Fluxo de dados

```
Screen (Composable)
    ↕ observa StateFlow
ViewModel
    ↕ chama suspend fun
Repository
    ↕ faz requisição HTTP
Retrofit + MockInterceptor
    ↕ intercepta e responde com dados mockados
MockDataStore (SharedPreferences)
```

---

## 🛠️ Tecnologias & Bibliotecas

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Linguagem** | Kotlin | 2.0.0 |
| **UI Framework** | Jetpack Compose | BOM 2024.08.00 |
| **Design System** | Material Design 3 | — |
| **Navegação** | Navigation Compose | 2.7.7 |
| **Arquitetura** | ViewModel + StateFlow | 2.8.4 |
| **Rede** | Retrofit 2 | 2.11.0 |
| **HTTP Client** | OkHttp 3 | 4.12.0 |
| **Serialização** | Gson | 2.11.0 |
| **Coroutines** | Kotlinx Coroutines | 1.8.1 |
| **Persistência** | SharedPreferences (via DataStore) | 1.1.1 |
| **Build System** | Gradle (Kotlin DSL) | 8.5.2 |
| **Min SDK** | Android 8.0 (Oreo) | API 26 |
| **Target SDK** | Android 15 | API 35 |

---

## 🔌 Camada de Dados Mock

Como o backend de IA ainda está em desenvolvimento, o app utiliza uma camada de mock completa que simula um servidor real:

- **`MockInterceptor`** — intercepta todas as requisições Retrofit antes de chegarem à rede e retorna respostas JSON simuladas com códigos HTTP corretos (200, 401, 404, 409)
- **`MockDataStore`** — singleton com estado em memória que persiste dados via `SharedPreferences`, simulando um banco de dados real. Suporta:
  - Criação e autenticação de usuários
  - CRUD completo de terrenos por usuário
  - Geração de dados climáticos variados baseados em latitude, tipo de cultura e temperatura
  - Cálculo de risco de incêndio com múltiplos fatores (temperatura, umidade, tipo de cultura, tamanho)

### Usuários de teste pré-cadastrados

| E-mail | Senha |
|--------|-------|
| `joao.pereira@chamazero.com.br` | `123456` |
| `maria.silva@chamazero.com.br` | `123456` |

---

## 🚀 Como executar

### Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 11+
- Android SDK API 26+
- Dispositivo físico ou emulador com Android 8.0+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/[seu-usuario]/chamazero-android.git

# 2. Abra no Android Studio
# File > Open > selecione a pasta do projeto

# 3. Aguarde o Gradle sync

# 4. Execute no dispositivo/emulador
# Run > Run 'app'  (ou Shift+F10)
```

> Não é necessária nenhuma configuração de API key ou variável de ambiente. O app funciona completamente offline com os dados mockados.

---

## 📐 Padrões de Código

- **Idioma:** Todo o código, variáveis e comentários estão em **inglês**
- **Componentização:** Componentes de UI reutilizáveis isolados em `ui/component/`
- **Estado:** Gerenciado exclusivamente via `StateFlow` nos ViewModels
- **Validação:** Feita na camada ViewModel, não nos composables
- **Navegação:** Centralizada no `AppNavGraph` com rotas tipadas
- **Sem comentários no código:** O código é autoexplicativo por nomenclatura

---

## 🎨 Design System

A paleta de cores foi escolhida para remeter ao universo do agronegócio e à natureza:

| Token | Cor | Uso |
|-------|-----|-----|
| `GreenPrimary` | `#2E7D32` | Botões, seleção ativa |
| `GreenBackground` | `#F0F7F0` | Fundo das telas |
| `GreenSurface` | `#E8F5E9` | Cards, chips de clima |
| `GreenGradientStart` | `#4CAF50` | Gradiente do logo |
| `GreenGradientEnd` | `#8B6914` | Gradiente do logo |
| `OrangeRisk` | `#F57C00` | Indicador de médio risco |
| `RedLogout` | `#E53935` | Botão de sair / alto risco |

---

## 📄 Licença

Este projeto foi desenvolvido exclusivamente para fins acadêmicos como parte da **Global Solution 2026** da FIAP. Todos os direitos reservados aos autores.

---

<div align="center">

Desenvolvido com ❤️ por alunos da **FIAP** — Turma 3ESOA · Global Solution 2026

</div>
