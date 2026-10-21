group = "app.unscroll"

patches {
    about {
        name = "Unscroll"
        description = "TikTok without doomscrolling. Keep your friends. Keep creating. Lose the feed."
        source = "https://github.com/marrttao/NoScrollingTiktok-"
        author = "Unscroll contributors"
        contact = "https://github.com/marrttao/NoScrollingTiktok-/issues"
        website = "https://github.com/marrttao/NoScrollingTiktok-"
        license = "GNU General Public License v3.0, with additional GPL section 7 requirements"
    }
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}

