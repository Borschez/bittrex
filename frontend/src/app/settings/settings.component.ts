import { Component, OnInit, Input } from '@angular/core';
import {Settings} from "../model/settings";
import {SettingsService} from "../services/settings.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  @Input() settings: Settings;

  constructor(
    private settingsService: SettingsService
  ) { }

  ngOnInit() {
    this.settingsService.getSettings().subscribe(settings => this.settings = settings);
  }

  save(): void {
    this.settingsService.updateSettings(this.settings).subscribe();
  }

}
