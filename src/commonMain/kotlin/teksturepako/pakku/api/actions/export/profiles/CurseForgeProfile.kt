package teksturepako.pakku.api.actions.export.profiles

import teksturepako.pakku.api.actions.export.ExportProfile
import teksturepako.pakku.api.actions.export.rules.createCfModpackModel
import teksturepako.pakku.api.actions.export.rules.replacementRule
import teksturepako.pakku.api.actions.export.rules.ruleOfCfMissingProjects
import teksturepako.pakku.api.actions.export.rules.ruleOfCfModpack
import teksturepako.pakku.api.data.ConfigFile
import teksturepako.pakku.api.data.LockFile
import teksturepako.pakku.api.platforms.CurseForge
import teksturepako.pakku.compat.exportFileDirector

class CurseForgeProfile(lockFile: LockFile, configFile: ConfigFile) : ExportProfile(
    name = CurseForge.serialName,
    rules = listOf(
        lockFile.getFirstMcVersion()?.let {
            val modpackModel = createCfModpackModel(it, lockFile, configFile)
            ruleOfCfModpack(modpackModel)
        },
        if (lockFile.getAllProjects().any { "filedirector" in it }) exportFileDirector(excludedProviders = setOf(CurseForge))
        else ruleOfCfMissingProjects(),
        replacementRule()
    ),
    dependsOn = CurseForge
)
{
    companion object
    {
        val NAME = CurseForge.serialName

        init
        {
            all[NAME] = ::CurseForgeProfile
        }
    }
}
