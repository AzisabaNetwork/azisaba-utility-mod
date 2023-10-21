# InterChatMod

[InterChat](https://github.com/AzisabaNetwork/InterChat) (ギルドチャット)をクライアントでも使えるようにするもの

## 共通

- APIキーはアジ鯖内で`/apikey`をすることで入手可能。
- `/cgs <ギルド> [メッセージ]`でギルドを選択、もしくはギルド内に発言。
- `/cg <メッセージ>`で事前に`/cgs`したギルドに発言。
- 「Chat without command」でデフォルトのチャットがギルドチャットになる。先頭に「!」をつけると一時的に無効化される。

## Blueberry

### 前提Mod

N/A

### 手順

1. Mod設定にAPIキーを入れる

## Fabric

### 前提Mod

- [fabric-api](https://modrinth.com/mod/fabric-api)
- [owo-lib](https://modrinth.com/mod/owo-lib)

### あったほうがいいMod

- [Mod Menu](https://modrinth.com/mod/modmenu)

### 手順

1. Mod Menuが入ってる場合は設定画面を使用可能、それ以外の場合はゲーム内で`/reconnectinterchat <apiキー>`で可能

## Building

For `blueberry-*` projects, please do `gradlew shadowJar`.

For `fabric-*` projects, please do `gradlew build`. (don't use `-all` jars!)
