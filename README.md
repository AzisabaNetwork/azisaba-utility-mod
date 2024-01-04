# AzisabaUtilityMod

## 共通

- APIキーはアジ鯖内で`/apikey`をすることで入手可能。

## Blueberry

### 前提Mod

N/A

### 手順

1. Mod設定にAPIキーを入れる

## Fabric

### 1.19.4以上

#### 前提Mod

- [fabric-api](https://modrinth.com/mod/fabric-api)
- [owo-lib](https://modrinth.com/mod/owo-lib/version/0.11.3+1.20.2)

#### あったほうがいいMod

- [Mod Menu](https://modrinth.com/mod/modmenu)

### 1.18.2以下

#### 前提Mod

- [fabric-api](https://modrinth.com/mod/fabric-api)
- [Mod Menu](https://modrinth.com/mod/modmenu)

### 手順

1. Mod Menuが入ってる場合は設定画面を使用可能

## Building

For `blueberry-*` projects, please do `gradlew shadowJar`.

For `fabric-*` projects, please do `gradlew build`. (don't use `-all` jars!)
