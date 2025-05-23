package ru.hoster.inprogress.di // Ваш пакет di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.hoster.inprogress.data.repository.FirestoreUserRepository
import ru.hoster.inprogress.domain.model.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule { // Можно объединить с AuthModule или AppModule, если они абстрактные

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        firestoreUserRepository: FirestoreUserRepository
    ): UserRepository



    // ... другие Binds для репозиториев (GroupRepository, ActivityRepository и т.д.)
}