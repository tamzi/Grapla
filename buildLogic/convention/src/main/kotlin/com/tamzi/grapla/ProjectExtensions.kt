package com.tamzi.grapla

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * The `libs` extension provides access to the `libs` [VersionCatalog] from the [VersionCatalogsExtension].
 *
 * The selected code defines an extension property libs for the Project class in Gradle.
 * This property is used to access a VersionCatalog named "libs" from the project's extensions.
 * The libs property is defined as a read-only property using the val keyword.
 * The property type is VersionCatalog, which represents a collection of versions for dependencies.
 *
 * It is an extension property, meaning it adds functionality to the Project class without
 * modifying its original definition. The property is accessed using the get() method,
 * which retrieves the VersionCatalog instance.
 *
 *      val Project.libs
 *          get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
 *
 * In the `get()` method, the extensions property of the Project class is used to get
 * the VersionCatalogsExtension instance.
 * The getByType method is called with VersionCatalogsExtension::class as the argument to retrieve
 * the extension of the specified type. Finally, the named("libs") method is called on the
 * VersionCatalogsExtension instance to get the VersionCatalog named "libs".
 * This setup allows developers to easily access the "libs" version catalog in their
 * Gradle build scripts by simply referring to project.libs.
 * This can be particularly useful for managing dependencies and versions in a centralized manner.
 *
 */
val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
