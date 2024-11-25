//
//  AppIntent.swift
//  WaybackWidget
//
//  Created by Javier Manrique Pellejero on 24/11/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import WidgetKit
import AppIntents

struct ConfigurationAppIntent: WidgetConfigurationIntent {
    static var title: LocalizedStringResource { "Configuration" }
    static var description: IntentDescription { "This is an example widget." }

    // An example configurable parameter.
    @Parameter(title: "Favorite Emoji", default: "😃")
    var favoriteEmoji: String
    
    func perform() async throws -> some IntentResult {
        return .result()
    }
}
